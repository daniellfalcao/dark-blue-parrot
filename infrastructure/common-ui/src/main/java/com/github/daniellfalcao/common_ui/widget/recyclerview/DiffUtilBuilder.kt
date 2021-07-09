package com.github.daniellfalcao.common_ui.widget.recyclerview

import androidx.recyclerview.widget.DiffUtil

interface DiffUtilBuilder {

    /**
     * Generates a instance of [DiffUtil.Callback] to be used in [ParrotRecyclerView.Adapter]
     *
     * */
    fun <T> build(newList: List<T>, oldList: List<T>): DiffUtil.Callback

}