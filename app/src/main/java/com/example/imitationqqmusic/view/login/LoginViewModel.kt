package com.example.imitationqqmusic.view.login

import android.content.Context
import androidx.lifecycle.*
import com.example.imitationqqmusic.model.room_bean.User
import com.example.imitationqqmusic.model.room_bean.UserDao
import com.example.imitationqqmusic.model.room_bean.UserDataBase

class LoginViewModel : ViewModel() {

    companion object {
        private var viewModel: LoginViewModel? = null
        fun newInstance(owner: ViewModelStoreOwner) = synchronized(this) {
            viewModel ?: kotlin.run {
                viewModel = ViewModelProvider(owner).get(LoginViewModel::class.java)
            }
            viewModel
        }
    }

    private lateinit var userDataBase: UserDataBase
    private lateinit var userDao: UserDao

    private var _loginEnable = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val loginEnable: LiveData<Boolean> = _loginEnable

    fun setUserDatabase(context: Context) {
        userDataBase = UserDataBase.newInstance(context)!!
        userDao = userDataBase.getUserDao()
    }

    var loginListener: LoginActivity.OnLoginClick? = null
    var registeredListener: LoginActivity.OnRegisteredClick? = null

    private var _isLoginOrRegistered = MutableLiveData<Boolean>().also {
        it.value = true
    }
    val isLoginOrRegistered: LiveData<Boolean> = _isLoginOrRegistered

    private var _toastString = MutableLiveData<String>()
    val toastString: LiveData<String> = _toastString

    private var _cleanEt = MutableLiveData<Boolean>()
    val cleanEt: LiveData<Boolean> = _cleanEt

    private fun cleanEt() {
        _cleanEt.postValue(true)
    }

    var user: User? = null

    var isFirst = true

    fun changeMode(boolean: Boolean) {
        _isLoginOrRegistered.postValue(boolean)
    }

    fun toastMessage(message: String) {
        _toastString.postValue(message)
    }

    fun isHaveThisUser(user: User) {
        Thread(Runnable {
            println("================userDao.getUserById(user.userId): ${userDao.getUserById(user.userId)}")
            if (userDao.getUserById(user.userId) == null) {
                userDao.insert(user)
                changeMode(true)
                cleanEt()
                toastMessage("注册成功")
            } else {
                toastMessage("用户名已经存在")
            }
        }).start()
    }

    fun userExist(user: User) {
        Thread(Runnable {
            val dataBaseUser = userDao.getUserById(user.userId)
            println("================dataBaseUser: $dataBaseUser")
            if (dataBaseUser != null && user.passWord == dataBaseUser.passWord) {
                this.user = dataBaseUser
                _loginEnable.postValue(true)
            } else {
                _loginEnable.postValue(false)
                toastMessage("用户名或密码错误")
            }
        }).start()
    }

    fun clearAll() {
        Thread(Runnable {
            userDao.deleteAll()
        }).start()
    }
}