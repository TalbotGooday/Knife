package io.github.mthli.knifedemo.utils.extensions

import android.content.Context
import android.widget.Toast


fun Context.toast(message: String?) {
	Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
	Toast.makeText(this, resId, duration).show()
}
