package com.piloterr.android.piloterr.ui.activities

import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.UserRepository
import com.piloterr.android.piloterr.databinding.ActivityFixcharacterBinding
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.user.Stats
import com.piloterr.android.piloterr.models.user.User
import com.piloterr.android.piloterr.modules.AppModule
import com.piloterr.android.piloterr.ui.views.PiloterrIconsHelper
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_fixcharacter.*
import javax.inject.Inject
import javax.inject.Named

class FixCharacterValuesActivity: BaseActivity() {

    private lateinit var binding: ActivityFixcharacterBinding
    @Inject
    lateinit var repository: UserRepository

    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String

    override fun getLayoutResId(): Int = R.layout.activity_fixcharacter

    override fun getContentView(): View {
        binding = ActivityFixcharacterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.fix_character_values)
        setupToolbar(binding.toolbar)

        compositeSubscription.add(repository.getUser(userId).firstElement().subscribe(Consumer {
            user = it
        }, RxErrorHandler.handleEmptyError()))

        setIconBackground(binding.healthIconBackgroundView, ContextCompat.getColor(this, R.color.red_500))
        setIconBackground(binding.experienceIconBackgroundView, ContextCompat.getColor(this, R.color.yellow_500))
        setIconBackground(binding.manaIconBackgroundView, ContextCompat.getColor(this, R.color.blue_500))
        setIconBackground(binding.goldIconBackgroundView, ContextCompat.getColor(this, R.color.yellow_500))
        setIconBackground(binding.streakIconBackgroundView, ContextCompat.getColor(this, R.color.gray_400))

        binding.healthIconView.setImageBitmap(PiloterrIconsHelper.imageOfHeartLightBg())
        binding.experienceIconView.setImageBitmap(PiloterrIconsHelper.imageOfExperience())
        binding.manaIconView.setImageBitmap(PiloterrIconsHelper.imageOfMagic())
        binding.goldIconView.setImageBitmap(PiloterrIconsHelper.imageOfGold())
        binding.levelIconView.setImageBitmap(PiloterrIconsHelper.imageOfRogueLightBg())
        binding.streakIconView.setImageResource(R.drawable.achievement_thermometer)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_save_changes) {
            @Suppress("DEPRECATION")
            val progressDialog = ProgressDialog.show(this, getString(R.string.saving), "")
            val userInfo = HashMap<String, Any>()
            userInfo["stats.hp"] = binding.healthEditText.getDoubleValue()
            userInfo["stats.exp"] = binding.experienceEditText.getDoubleValue()
            userInfo["stats.gp"] = binding.goldEditText.getDoubleValue()
            userInfo["stats.mp"] = binding.manaEditText.getDoubleValue()
            userInfo["stats.lvl"] = binding.levelEditText.getDoubleValue().toInt()
            userInfo["achievements.streak"] = binding.streakEditText.getDoubleValue().toInt()
            compositeSubscription.add(repository.updateUser(user, userInfo).subscribe(Consumer {}, RxErrorHandler.handleEmptyError(), Action {
                progressDialog.dismiss()
                finish()
            }))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private var user: User? = null
    set(value) {
        field = value
        if (value != null) {
            updateFields(value)
        }
    }

    private fun updateFields(user: User) {
        val stats = user.stats ?: return
        binding.healthEditText.setText(stats.hp.toString())
        binding.experienceEditText.setText(stats.exp.toString())
        binding.goldEditText.setText(stats.gp.toString())
        binding.manaEditText.setText(stats.mp.toString())
        binding.levelEditText.setText(stats.lvl.toString())
        binding.streakEditText.setText(user.streakCount.toString())

        when (stats.habitClass) {
            Stats.WARRIOR -> {
                setIconBackground(levelIconBackgroundView, ContextCompat.getColor(this, R.color.red_500))
                binding.levelIconView.setImageBitmap(PiloterrIconsHelper.imageOfWarriorLightBg())
            }
            Stats.MAGE -> {
                setIconBackground(levelIconBackgroundView, ContextCompat.getColor(this, R.color.blue_500))
                binding.levelIconView.setImageBitmap(PiloterrIconsHelper.imageOfMageLightBg())
            }
            Stats.HEALER -> {
                setIconBackground(levelIconBackgroundView, ContextCompat.getColor(this, R.color.yellow_500))
                binding.levelIconView.setImageBitmap(PiloterrIconsHelper.imageOfHealerLightBg())
            }
            Stats.ROGUE -> {
                setIconBackground(levelIconBackgroundView, ContextCompat.getColor(this, R.color.brand_500))
                binding.levelIconView.setImageBitmap(PiloterrIconsHelper.imageOfRogueLightBg())
            }
        }
    }

    private fun setIconBackground(view: View, color: Int) {
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.layout_rounded_bg)
        backgroundDrawable?.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        backgroundDrawable?.alpha = 50
        view.background = backgroundDrawable
    }

    private fun EditText.getDoubleValue(): Double {
        val stringValue = this.text.toString()
        return try {
            stringValue.toDouble()
        } catch (_: NumberFormatException) {
            0.0
        }
    }

}