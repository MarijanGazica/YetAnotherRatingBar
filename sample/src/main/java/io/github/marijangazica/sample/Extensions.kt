package io.github.marijangazica.sample

import android.view.View

inline fun View.onClick(crossinline onClick: () -> Unit) = setOnClickListener { onClick() }

