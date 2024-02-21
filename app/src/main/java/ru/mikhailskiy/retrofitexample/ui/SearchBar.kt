package ru.mikhailskiy.retrofitexample.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import ru.mikhailskiy.retrofitexample.R
import ru.mikhailskiy.retrofitexample.databinding.SearchToolbarBinding

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    lateinit var binding: SearchToolbarBinding

    val editText: EditText by lazy { binding.searchEditText }

    val onTextChangedObservable by lazy {
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            editText.doAfterTextChanged { text ->
                subscriber.onNext(text.toString())
            }
        })
    }

    private var hint: String = ""
    private var isCancelVisible: Boolean = true

    init {
        LayoutInflater.from(context).inflate(R.layout.search_toolbar, this)
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.SearchBar).apply {
                hint = getString(R.styleable.SearchBar_hint).orEmpty()
                isCancelVisible = getBoolean(R.styleable.SearchBar_cancel_visible, true)
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = SearchToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        binding.searchEditText.hint = hint
        binding.deleteTextButton.setOnClickListener {
            binding.searchEditText.text.clear()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.searchEditText.afterTextChanged { text ->
            if (!text.isNullOrEmpty() && !binding.deleteTextButton.isVisible) {
                binding.deleteTextButton.visibility = View.VISIBLE
            }
            if (text.isNullOrEmpty() && binding.deleteTextButton.isVisible) {
                binding.deleteTextButton.visibility = View.GONE
            }
        }
    }
}