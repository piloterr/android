package com.piloterr.android.piloterr.ui.fragments.inventory.equipment

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.InventoryRepository
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.models.inventory.Equipment
import com.piloterr.android.piloterr.models.user.Items
import com.piloterr.android.piloterr.ui.adapter.inventory.EquipmentRecyclerViewAdapter
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import com.piloterr.android.piloterr.ui.fragments.inventory.customization.AvatarCustomizationFragmentArgs
import com.piloterr.android.piloterr.ui.helpers.SafeDefaultItemAnimator
import io.reactivex.functions.Consumer
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import javax.inject.Inject

class EquipmentDetailFragment : BaseMainFragment() {

    @Inject
    lateinit var inventoryRepository: InventoryRepository

    var type: String? = null
    var equippedGear: String? = null
    var isCostume: Boolean? = null

    private var adapter: EquipmentRecyclerViewAdapter = EquipmentRecyclerViewAdapter(null, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        this.adapter.equippedGear = this.equippedGear
        this.adapter.isCostume = this.isCostume
        this.adapter.type = this.type
        compositeSubscription.add(this.adapter.equipEvents.flatMapMaybe { key -> inventoryRepository.equipGear(user, key, isCostume ?: false).firstElement() }
                .subscribe(Consumer { }, RxErrorHandler.handleEmptyError()))
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showsBackButton = true
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val args = EquipmentDetailFragmentArgs.fromBundle(it)
            type = args.type
            isCostume = args.isCostume
            equippedGear = args.equippedGear
        }

        recyclerView.adapter = this.adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL))
        recyclerView.itemAnimator = SafeDefaultItemAnimator()

        type?.let { type -> inventoryRepository.getOwnedEquipment(type).firstElement().subscribe(Consumer<RealmResults<Equipment>> { this.adapter.updateData(it) }, RxErrorHandler.handleEmptyError()) }
    }

    override fun onDestroy() {
        inventoryRepository.close()
        super.onDestroy()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }
}
