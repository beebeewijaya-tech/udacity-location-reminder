package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource(private var reminders : LinkedHashMap<String, ReminderDTO> = LinkedHashMap()) : ReminderDataSource {
    companion object {
        const val ERROR_MESSAGE = "Something wrong with reminders"
    }

    private var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error(ERROR_MESSAGE)
        }
        return Result.Success(reminders.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error(ERROR_MESSAGE)
        }
        reminders[id]?.let {
            return Result.Success(it)
        }
        return Result.Error(ERROR_MESSAGE)
    }

    override suspend fun deleteAllReminders() {
        reminders.clear()
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
}