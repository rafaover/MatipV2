package com.exercise.matipv2.data.remote

import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.local.model.TipList

fun TipList.toBackupDto() = ListBackupDto(id = id, name = name, userId = userId, cloudId = cloudId)

fun ListBackupDto.toTipList() = TipList(id = id, name = name, userId = userId, cloudId = cloudId)

fun Tip.toBackupDto() = TipBackupDto(
    id = id,
    tipAmount = tipAmount,
    tipPercent = tipPercent,
    listId = listId,
    dateCreated = dateCreated,
    userId = userId,
    cloudId = cloudId,
)

fun TipBackupDto.toTip() = Tip(
    id = id,
    tipAmount = tipAmount,
    tipPercent = tipPercent,
    listId = listId,
    dateCreated = dateCreated,
    userId = userId,
    cloudId = cloudId,
)
