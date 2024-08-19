package com.didebbo.mappify.presentation.view.fragment.destination.prelogin

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.databinding.ColorPickerItemBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.didebbo.mappify.databinding.RegisterPageLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterPage: BaseFragmentDestination<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var binding: RegisterPageLayoutBinding
    private lateinit var colorPicker: RecyclerView

    private val _colorPickerSelected: MutableLiveData<AvatarColor> = MutableLiveData(AvatarColor("Gray", R.color.avatar_gray))
    private val colorPickerSelected: LiveData<AvatarColor> get() = _colorPickerSelected
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterPageLayoutBinding.inflate(inflater,container,false)
        colorPicker = binding.colorPickerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureColorPicker()

        colorPickerSelected.observe(viewLifecycleOwner) {
            context?.getColor(it.resId)?.let { color ->
                binding.avatarNameTextView.backgroundTintList = ColorStateList.valueOf(color)
            }
        }

        binding.nameTextField.doOnTextChanged { n, _, _, _ ->
            val name = n?.first()?.uppercase() ?: "U"
            val surname = binding.surnameTextField.text.firstOrNull()?.uppercase() ?: "N"
         binding.avatarNameTextView.text = "$name$surname"
        }

        binding.surnameTextField.doOnTextChanged { s, _, _, _ ->
            val name = binding.nameTextField.text.firstOrNull()?.uppercase() ?: "U"
            val surname = s?.first()?.uppercase() ?: "N"
            binding.avatarNameTextView.text = "$name$surname"
        }

        binding.emailRegisterButton.setOnClickListener {

            parentActivity?.hideSystemKeyboard()

            val userAuth = UserAuth(
                name = binding.nameTextField.text.toString(),
                surname = binding.surnameTextField.text.toString(),
                email = binding.emailTextField.text.toString(),
                password = binding.passwordTextField.text.toString(),
                avatarColor = colorPickerSelected.value?.resId
            )

            parentActivity?.loaderCoroutineScope {
                viewModel.createUserWithEmailAndPassword(userAuth).let { result ->
                    result.exceptionOrNull()?.let {
                        val message = it.localizedMessage ?: "Undefined Error"
                        parentActivity?.showAlertView(message)
                        return@loaderCoroutineScope
                    }
                    result.getOrNull()?.let {
                        navController?.popBackStack()
                    }
                }
            }
        }
    }

    private fun configureColorPicker() {
        val data = AvatarColor.AvatarColors.entries.map {
            ColorPickerAdapter.VH.VD(
                name = it.avatarColor.name,
                resId = it.avatarColor.resId,
                onClick = {
                    _colorPickerSelected.postValue(it.avatarColor)
                }
            )
        }
        colorPicker.adapter = ColorPickerAdapter(this, data)
    }
}

class ColorPickerAdapter(private val parent: Fragment, private val data: List<VH.VD>): RecyclerView.Adapter<ColorPickerAdapter.VH>() {
    class VH(private val parent: Fragment, private val binding: ColorPickerItemBinding): RecyclerView.ViewHolder(binding.root) {

        data class VD (
            val name: String,
            val resId: Int,
            val onClick: ()->Unit
        )
        fun bind(data: VD) {
            parent.context?.getColor(data.resId)?.let {
                binding.colorIconPicker.imageTintMode = PorterDuff.Mode.DARKEN
                binding.colorIconPicker.imageTintList = ColorStateList.valueOf(it)
                binding.colorIconPicker.setOnClickListener{
                    data.onClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ColorPickerItemBinding.inflate(LayoutInflater.from(parent.context))
        return VH(this.parent, binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(data[position])
    }
}