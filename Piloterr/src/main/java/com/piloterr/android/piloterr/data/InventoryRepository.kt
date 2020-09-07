package com.piloterr.android.piloterr.data

import com.piloterr.android.piloterr.models.inventory.*
import com.piloterr.android.piloterr.models.responses.BuyResponse
import com.piloterr.android.piloterr.models.responses.FeedResponse
import com.piloterr.android.piloterr.models.shops.Shop
import com.piloterr.android.piloterr.models.shops.ShopItem
import com.piloterr.android.piloterr.models.user.*
import io.reactivex.Flowable
import io.realm.RealmResults


interface InventoryRepository : BaseRepository {

    fun getArmoireRemainingCount(): Long

    fun getInAppRewards(): Flowable<RealmResults<ShopItem>>
    fun getOwnedEquipment(): Flowable<RealmResults<Equipment>>

    fun getMounts(): Flowable<RealmResults<Mount>>

    fun getOwnedMounts(): Flowable<RealmResults<OwnedMount>>

    fun getPets(): Flowable<RealmResults<Pet>>

    fun getOwnedPets(): Flowable<RealmResults<OwnedPet>>
    fun getQuestContent(key: String): Flowable<QuestContent>
    fun getQuestContent(keys: List<String>): Flowable<RealmResults<QuestContent>>

    fun getEquipment(searchedKeys: List<String>): Flowable<RealmResults<Equipment>>
    fun retrieveInAppRewards(): Flowable<List<ShopItem>>

    fun getOwnedEquipment(type: String): Flowable<RealmResults<Equipment>>
    fun getEquipmentType(type: String, set: String): Flowable<RealmResults<Equipment>>

    fun getOwnedItems(itemType: String, includeZero: Boolean = false): Flowable<RealmResults<OwnedItem>>
    fun getOwnedItems(includeZero: Boolean = false): Flowable<Map<String, OwnedItem>>

    fun getEquipment(key: String): Flowable<Equipment>

    fun openMysteryItem(user: User?): Flowable<Equipment>

    fun saveEquipment(equipment: Equipment)
    fun getMounts(type: String?, group: String?, color: String?): Flowable<RealmResults<Mount>>
    fun getPets(type: String?, group: String?, color: String?): Flowable<RealmResults<Pet>>

    fun updateOwnedEquipment(user: User)

    fun changeOwnedCount(type: String, key: String, amountToAdd: Int)

    fun sellItem(user: User?, type: String, key: String): Flowable<User>
    fun sellItem(user: User?, item: OwnedItem): Flowable<User>

    fun equipGear(user: User?, equipment: String, asCostume: Boolean): Flowable<Items>
    fun equip(user: User?, type: String, key: String): Flowable<Items>

    fun feedPet(pet: Pet, food: Food): Flowable<FeedResponse>

    fun hatchPet(egg: Egg, hatchingPotion: HatchingPotion, successFunction: () -> Unit): Flowable<Items>

    fun inviteToQuest(quest: QuestContent): Flowable<Quest>

    fun buyItem(user: User?, id: String, value: Double, purchaseQuantity: Int): Flowable<BuyResponse>

    fun retrieveShopInventory(identifier: String): Flowable<Shop>
    fun retrieveMarketGear(): Flowable<Shop>

    fun purchaseMysterySet(categoryIdentifier: String): Flowable<Any>

    fun purchaseHourglassItem(purchaseType: String, key: String): Flowable<Any>

    fun purchaseQuest(key: String): Flowable<Any>
    fun purchaseSpecialSpell(key: String): Flowable<Any>

    fun purchaseItem(purchaseType: String, key: String, purchaseQuantity: Int): Flowable<Any>

    fun togglePinnedItem(item: ShopItem): Flowable<List<ShopItem>>
    fun getItems(itemClass: Class<out Item>, keys: Array<String>): Flowable<out RealmResults<out Item>>
    fun getItems(itemClass: Class<out Item>): Flowable<out RealmResults<out Item>>
    fun getLatestMysteryItem(): Flowable<Equipment>
    fun getItem(type: String, key: String): Flowable<Item>
}