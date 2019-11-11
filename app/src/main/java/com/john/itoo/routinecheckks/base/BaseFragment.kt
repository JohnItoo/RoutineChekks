package com.john.itoo.routinecheckks.base

import androidx.fragment.app.Fragment
import com.john.itoo.routinecheckks.MainActivity

abstract class BaseFragment : Fragment() {

    protected val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }
}
