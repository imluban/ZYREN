package com.lubannoor.pulse.eq.data

import kotlinx.serialization.Serializable

@Serializable
enum class FilterType {

  PK,
  LSC,
  HSC,
  LPQ,
  HPQ
}
