package com.piloterr.android.piloterr.ui.fragments.support

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.data.FAQRepository
import com.piloterr.android.piloterr.extensions.inflate
import com.piloterr.android.piloterr.helpers.RxErrorHandler
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import com.piloterr.android.piloterr.ui.helpers.MarkdownParser
import com.piloterr.android.piloterr.ui.helpers.bindOptionalView
import com.piloterr.android.piloterr.ui.helpers.resetViews
import io.reactivex.functions.Consumer
import javax.inject.Inject

class FAQDetailFragment : BaseMainFragment() {
    @Inject
    lateinit var faqRepository: FAQRepository

    private val questionTextView: TextView? by bindOptionalView(R.id.questionTextView)
    private val answerTextView: TextView? by bindOptionalView(R.id.answerTextView)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hidesToolbar = true
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflate(R.layout.fragment_faq_detail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetViews()

        arguments?.let {
            val args = FAQDetailFragmentArgs.fromBundle(it)
            compositeSubscription.add(faqRepository.getArticle(args.position).subscribe(Consumer { faq ->
                this.questionTextView?.text = faq.question
                this.answerTextView?.text = MarkdownParser.parseMarkdown(faq.answer)
            }, RxErrorHandler.handleEmptyError()))
        }

        this.answerTextView?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }
}