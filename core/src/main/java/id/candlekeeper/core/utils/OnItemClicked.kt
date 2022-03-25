package id.candlekeeper.core.utils

import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Endorse

interface OnItemClicked {
    //data content
    fun onEventClick(data: Carousel) {}
    fun onEventClick(data: Heroes) {}
    fun onEventClick(data: Skins) {}

    //data income
    fun onEventClick(data: Endorse) {}

    //data additional
    fun onEventClick(data: UrlTutorial) {}
}