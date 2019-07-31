package com.tonyyang.common.itemselector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View


class SeparatorDecoration
@JvmOverloads constructor(
    width: Float = 1F,
    backgroundColor: Int = Color.WHITE,
    separatorColor: Int = Color.GRAY,
    private val mMarginLeft: Float = 0F,
    private val mMarginRight: Float = 0F,
    private val mHeaderCnt: Int = 0
) : RecyclerView.ItemDecoration() {

    private val mSeparatorPaint = Paint()

    private val mBackgroundPaint = Paint()

    private var headerViews: MutableList<View> = mutableListOf()

    init {
        mSeparatorPaint.apply {
            this.color = separatorColor
            this.strokeWidth = width
        }
        mBackgroundPaint.apply {
            this.color = backgroundColor
            this.strokeWidth = width
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val params = view.layoutParams as RecyclerView.LayoutParams

        // we want to retrieve the position in the list
        val position = params.viewAdapterPosition

        // and add a separator to any view but the first and last one
        if (position > 0 && position < state.itemCount) {
            outRect.set(0, 0, 0, mSeparatorPaint.strokeWidth.toInt()) // left, top, right, bottom
        } else {
            if (position < mHeaderCnt) {
                headerViews.add(view)
            }
            outRect.setEmpty() // 0, 0, 0, 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // we set the stroke width before, so as to correctly draw the line we have to offset by width / 2
        val offset = (mSeparatorPaint.strokeWidth / 2).toInt()

        // this will iterate over every visible view
        for (i in 0 until parent.childCount) {
            // get the view
            val view = parent.getChildAt(i)

            // skip first item view
            if (view == headerViews) {
                continue
            }

            val params = view.layoutParams as RecyclerView.LayoutParams

            // get the position
            val position = params.viewAdapterPosition

            // and finally draw the separator
            if (position < state.itemCount) {
                c.drawLine(
                    view.left.toFloat(), (view.bottom + offset).toFloat(),
                    view.left.toFloat() + mMarginLeft - 1, (view.bottom + offset).toFloat(), mBackgroundPaint
                )
                c.drawLine(
                    view.left.toFloat() + mMarginLeft, (view.bottom + offset).toFloat(),
                    view.right.toFloat() - mMarginRight, (view.bottom + offset).toFloat(), mSeparatorPaint
                )
            }
        }
    }

    /**
     * Builder to create a SeparatorDecoration.
     *
     * Create the builder with a context to configure a SeparatorDecoration.
     *
     * @param mContext the context
     */
    class Builder(private val mContext: Context) {
        private var mWidth: Float = 1F
        private var mBGColor: Int = Color.WHITE
        private var mColor: Int = Color.GRAY
        private var mMarginLeft = 0F
        private var mMarginRight = 0F
        private var mHeaderCnt = 0

        /**
         * Set the background color from a resource.
         *
         * @param bgColorResId the resource id to use
         * @return the builder
         */
        fun bgColorFromResources(bgColorResId: Int): Builder {
            mBGColor = mContext.resources.getColor(bgColorResId)
            return this
        }

        /**
         * Set the background color from a color.
         *
         * @param bgColor the color to use
         * @return the builder
         * @see android.graphics.Color
         */
        fun bgColor(bgColor: Int): Builder {
            mBGColor = bgColor
            return this
        }

        /**
         * Set the separator color from a resource.
         *
         * @param colorResId the resource id to use
         * @return the builder
         */
        fun colorFromResources(colorResId: Int): Builder {
            mColor = mContext.resources.getColor(colorResId)
            return this
        }

        /**
         * Set the separator color from a color.
         *
         * @param color the color to use
         * @return the builder
         * @see android.graphics.Color
         */
        fun color(color: Int): Builder {
            mColor = color
            return this
        }

        /**
         * Set the width of the separator.
         *
         * @param width the width in dp
         * @return the builder
         */
        fun width(width: Float): Builder {
            mWidth = width
            return this
        }

        /**
         * Set the margin of the separator
         *
         * @param margin the margin in dp
         * @return the builder
         */
        fun setMargin(margin: Float): Builder {
            setMargin(margin, margin)
            return this
        }

        /**
         * Set the margin of the separator
         *
         * @param left  the left margin in dp
         * @param right the right margin in dp
         * @return the builder
         */
        fun setMargin(left: Float, right: Float): Builder {
            val metrics = mContext.resources.displayMetrics
            mMarginLeft = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                left, metrics
            )
            mMarginRight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                right, metrics
            )
            return this
        }

        fun setHeaderCount(headerCount: Int): Builder {
            mHeaderCnt = headerCount
            return this
        }

        /**
         * Get the configured SeparatorDecoration.
         *
         * @return the separatorDecoration
         * @see SeparatorDecoration
         */
        fun build(): SeparatorDecoration {
            return SeparatorDecoration(mWidth, mBGColor, mColor, mMarginLeft, mMarginRight, mHeaderCnt)
        }
    }
}