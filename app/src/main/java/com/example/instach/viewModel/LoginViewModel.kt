package com.example.instach.viewModel

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.example.instach.`interface`.LoginResultCallBacks
import com.example.instach.model.User

class LoginViewModel(private val listener: LoginResultCallBacks) : ViewModel() {

    private val user: User

    init {
        this.user = User("", "", "", "", "", "", "")
    }

    val emailTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user.setEmail(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }

    val passwordTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user.setPassword(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }

    fun onLoginClicked(v: View) {
        var loginCode: Int = user.isDataValid()
        if (loginCode == 0) {
            listener.onError("Email must not be empty")
        }else if (loginCode == 1) {
            listener.onError("Wrong email pattern")
        }else if (loginCode == 2) {
            listener.onError("Password must be greater than 6")
        } else {
            listener.onSuccess("Login Successful")
        }
    }


}