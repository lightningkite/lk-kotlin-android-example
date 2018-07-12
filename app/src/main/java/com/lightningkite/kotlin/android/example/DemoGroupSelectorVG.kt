package com.lightningkite.kotlin.android.example

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import com.lightningkite.kotlin.android.example.random.ObservableList2VG
import lk.android.activity.access.ActivityAccess
import lk.android.extensions.selectableItemBackgroundResource
import lk.android.lifecycle.lifecycle
import lk.android.mighty.view.ViewGenerator
import lk.anko.adapters.observable.listAdapter
import lk.anko.extensions.anko
import lk.anko.extensions.verticalRecyclerView
import lk.kotlin.observable.property.StackObservableProperty
import lk.kotlin.observable.property.lifecycle.bind
import org.jetbrains.anko.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * A [ViewGenerator] for selecting which demo you want to view.
 * Created by jivie on 5/5/16.
 */
class DemoGroupSelectorVG(val stack: StackObservableProperty<ViewGenerator>) : ViewGenerator, HasTitle {

    override fun getTitle(resources: Resources): String = resources.getString(R.string.app_name)

    data class DemoGroup(val name: String, val demos: List<Demo>)
    data class Demo(
            val name: String,
            val groupName: String,
            val kClass: KClass<*>,
            val maker: (StackObservableProperty<ViewGenerator>) -> ViewGenerator
    )

    companion object {
        val commonPackage = "com.lightningkite.kotlin.android.example."
        fun KClass<*>.default(
                maker: (StackObservableProperty<ViewGenerator>) -> ViewGenerator = {
                    this.createInstance() as ViewGenerator
                }
        ) = Demo(
                name = simpleName!!.removeSuffix("VG").replace(Regex("[A-Z0-9]")) { " " + it.value }.trim(),
                kClass = this,
                groupName = this.qualifiedName!!.substringBeforeLast('.').removePrefix(commonPackage).split('.').joinToString(" ") { it.capitalize() },
                maker = maker
        )

        val demos = listOf<Demo>(
                com.lightningkite.kotlin.android.example.activity.access.ActivityAccessVG::class.default(),
                com.lightningkite.kotlin.android.example.animations.HeightAnimatorVG::class.default(),
                com.lightningkite.kotlin.android.example.animations.TransitionViewVG::class.default(),
                com.lightningkite.kotlin.android.example.animations.SwapViewVG::class.default(),
                com.lightningkite.kotlin.android.example.animations.observable.ExpandingExampleVG::class.default(),
                com.lightningkite.kotlin.android.example.design.extensions.DesignExtensionsVG::class.default(),
                com.lightningkite.kotlin.android.example.dialogs.DialogsVG::class.default(),
                com.lightningkite.kotlin.android.example.extensions.SelectorVG::class.default(),
                com.lightningkite.kotlin.android.example.extensions.StickyHeadersVG::class.default(),
                com.lightningkite.kotlin.android.example.image.loading.ImageLoadingVG::class.default(),
                com.lightningkite.kotlin.android.example.image.loading.observable.ImageLoadingVG::class.default(),
                com.lightningkite.kotlin.android.example.image.loading.observable.LargeListImagesVG::class.default(),
                com.lightningkite.kotlin.android.example.observable.SimpleObservablePropertyVG::class.default(),
                com.lightningkite.kotlin.android.example.observable.MultipleBindingsVG::class.default(),

                com.lightningkite.kotlin.android.example.random.CoordinatorLayoutTestVG::class.default(),
                com.lightningkite.kotlin.android.example.random.ExampleLoginVG::class.default(),
                com.lightningkite.kotlin.android.example.random.NetImageTestVG::class.default(),
                com.lightningkite.kotlin.android.example.random.NetworkListVG::class.default(),
                com.lightningkite.kotlin.android.example.random.ObservableListVG::class.default(),
                com.lightningkite.kotlin.android.example.random.ObservableList2VG::class.default { ObservableList2VG(it) }
        )
                .groupBy { it.groupName }
                .map {
                    DemoGroup(
                            name = it.key,
                            demos = it.value
                    )
                }
    }

    override fun invoke(access: ActivityAccess): View = access.context.anko().run {
        verticalLayout {

            textView(R.string.pick_a_demo) {
                minimumHeight = dip(48)
                padding = dip(16)
                gravity = Gravity.CENTER
            }

            verticalRecyclerView {
                adapter = listAdapter(demos) { itemObs ->
                    textView {
                        minimumHeight = dip(48)
                        padding = dip(16)
                        backgroundResource = selectableItemBackgroundResource
                        lifecycle.bind(itemObs) {
                            text = it.name
                        }
                        setOnClickListener { it: View? ->
                            val item = itemObs.value
                            if (item.demos.size == 1) {
                                stack.push(item.demos.first().maker.invoke(stack))
                            } else {
                                stack.push(DemoSelectorVG(stack, itemObs.value))
                            }
                        }
                    }.lparams(matchParent, wrapContent)
                }
            }
        }
    }
}

