package com.piloterr.android.piloterr.utils

import com.google.firebase.perf.FirebasePerformance
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.piloterr.android.piloterr.extensions.getAsString
import com.piloterr.android.piloterr.models.ContentGear
import com.piloterr.android.piloterr.models.ContentResult
import com.piloterr.android.piloterr.models.FAQArticle
import com.piloterr.android.piloterr.models.Skill
import com.piloterr.android.piloterr.models.inventory.*
import io.realm.RealmList
import java.lang.reflect.Type

class ContentDeserializer : JsonDeserializer<ContentResult> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ContentResult {
        val deserializeTrace = FirebasePerformance.getInstance().newTrace("ContentDeserialize")
        deserializeTrace.start()
        val result = ContentResult()
        val obj = json.asJsonObject

        result.potion = context.deserialize(obj.get("potion"), Equipment::class.java)
        result.armoire = context.deserialize(obj.get("armoire"), Equipment::class.java)
        result.gear = context.deserialize(obj.get("gear"), ContentGear::class.java)

        for (entry in obj.get("quests").asJsonObject.entrySet()) {
            result.quests.add(context.deserialize(entry.value, QuestContent::class.java))
            result.quests.forEach { it.key = it.key }
        }
        for (entry in obj.get("eggs").asJsonObject.entrySet()) {
            result.eggs.add(context.deserialize(entry.value, Egg::class.java))
        }
        for (entry in obj.get("food").asJsonObject.entrySet()) {
            result.food.add(context.deserialize(entry.value, Food::class.java))
        }
        for (entry in obj.get("hatchingPotions").asJsonObject.entrySet()) {
            result.hatchingPotions.add(context.deserialize(entry.value, HatchingPotion::class.java))
        }

        val pets = obj.getAsJsonObject("petInfo")
        for (key in pets.keySet()) {
            val pet = Pet()
            val petObj = pets.getAsJsonObject(key)
            pet.animal = petObj.getAsString("egg")
            pet.color = petObj.getAsString("potion")
            pet.key = petObj.getAsString("key")
            pet.text = petObj.getAsString("text")
            pet.type = petObj.getAsString("type")
            if (pet.type == "special") {
                pet.animal = pet.key.split("-")[0]
                pet.color = pet.key.split("-")[1]
            }
            if (pet.type == "premium") {
                pet.premium = true
            }
            result.pets.add(pet)
        }

        val mounts = obj.getAsJsonObject("mountInfo")
        for (key in mounts.keySet()) {
            val mount = Mount()
            val mountObj = mounts.getAsJsonObject(key)
            mount.animal = mountObj.getAsString("egg")
            mount.color = mountObj.getAsString("potion")
            mount.key = mountObj.getAsString("key")
            mount.text = mountObj.getAsString("text")
            mount.type = mountObj.getAsString("type")
            if (mount.type == "special") {
                mount.animal = mount.key.split("-")[0]
                mount.color = mount.key.split("-")[1]
            }
            if (mount.type == "premium") {
                mount.premium = true
            }
            result.mounts.add(mount)
        }
        for ((classname, value) in obj.getAsJsonObject("spells").entrySet()) {
            val classObject = value.asJsonObject

            for ((_, value1) in classObject.entrySet()) {
                val skillObject = value1.asJsonObject
                val skill = Skill()
                skill.key = skillObject.get("key").asString
                skill.text = skillObject.get("text").asString
                skill.notes = skillObject.get("notes").asString
                skill.key = skillObject.get("key").asString
                skill.target = skillObject.get("target").asString
                skill.habitClass = classname
                skill.mana = skillObject.get("mana").asInt

                val lvlElement = skillObject.get("lvl")

                if (lvlElement != null) {
                    skill.lvl = lvlElement.asInt
                }

                result.spells.add(skill)
            }
        }
        result.appearances = context.deserialize(obj.get("appearances"), object : TypeToken<RealmList<Customization>>() {}.type)
        result.backgrounds = context.deserialize(obj.get("backgrounds"), object : TypeToken<RealmList<Customization>>() {}.type)

        result.faq = context.deserialize(obj.get("faq"), object : TypeToken<RealmList<FAQArticle>>() {}.type)
        deserializeTrace.stop()
        return result
    }
}