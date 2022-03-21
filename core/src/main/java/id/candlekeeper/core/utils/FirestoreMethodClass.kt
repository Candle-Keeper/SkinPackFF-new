package id.candlekeeper.core.utils

object FirestoreMethodClass {

    private fun getFirebaseMethod(prefManager: PrefManager, name: String): Boolean {
        prefManager.spGetFirebaseMethod()?.map {
            if (it.name == name) {
                return it.isOnlineRequest!!
            }
        }
        return true

    }

    fun category(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "category")
    }

    fun heroes(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "heroes")
    }

    fun skins(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "skins")
    }

    fun addCategory(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "add_category")
    }

    fun addHeroes(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "add_heroes")
    }

    fun addSkins(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "add_skins")
    }

    fun endorse(prefManager: PrefManager): Boolean {
        return getFirebaseMethod(prefManager, "endorse")
    }
}