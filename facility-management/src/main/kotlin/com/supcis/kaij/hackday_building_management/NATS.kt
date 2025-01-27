package com.supcis.kaij.hackday_building_management

import io.nats.client.Connection
import io.nats.client.Nats
import org.koin.core.annotation.Single

@Single
fun nats(): Connection = Nats.connect()
