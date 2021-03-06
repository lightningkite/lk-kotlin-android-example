package com.lightningkite.kotlin.android.example.random

import android.view.Gravity
import android.view.View
import com.lightningkite.kotlin.android.example.styleDefault
import lk.android.activity.access.ActivityAccess
import lk.android.mighty.view.ViewGenerator
import lk.anko.extensions.anko
import lk.kotlin.observable.property.StackObservableProperty
import org.jetbrains.anko.button
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * Created as a dummy VC to test out the stack.
 * Created by josep on 11/6/2015.
 */
class StackDemoVG(val stack: StackObservableProperty<ViewGenerator>, val depth: Int = 1) : ViewGenerator {

    override fun invoke(access: ActivityAccess): View = access.context.anko().run {
        verticalLayout {
            gravity = Gravity.CENTER

            textView("This view controller has a depth of $depth.") {
                styleDefault()
            }

            button("Go deeper") {
                styleDefault()
                setOnClickListener { it: View? ->
                    stack.push(StackDemoVG(stack, depth + 1))
                }
            }

            button("Go back") {
                styleDefault()
                setOnClickListener { it: View? ->
                    stack.pop()
                }
            }
        }
    }
}