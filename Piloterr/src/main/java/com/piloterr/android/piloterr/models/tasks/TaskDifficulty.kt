package com.piloterr.android.piloterr.models.tasks

import com.piloterr.android.piloterr.R

enum class TaskDifficulty(val value: Float, val nameRes: Int) {
    TRIVIAL(0.1f, R.string.trivial),
    EASY(1f, R.string.easy),
    MEDIUM(1.5f, R.string.medium),
    HARD(2f, R.string.hard)
}