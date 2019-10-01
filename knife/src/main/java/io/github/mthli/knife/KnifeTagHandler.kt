/*
 * Copyright (C) 2015 Matthew Lee
 * Copyright (C) 2013-2015 Dominik Schürmann <dominik@dominikschuermann.de>
 * Copyright (C) 2013-2015 Juha Kuitunen
 * Copyright (C) 2013 Mohammed Lakkadshaw
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.mthli.knife

import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.StrikethroughSpan

import org.xml.sax.XMLReader

class KnifeTagHandler : Html.TagHandler {
	companion object {
		private const val BULLET_LI = "li"
		private const val STRIKE_THROUGH_S = "s"
		private const val STRIKE_THROUGH_STRIKE = "strike"
		private const val STRIKE_THROUGH_DEL = "del"

		private fun getLast(text: Editable, kind: Class<*>): Any? {
			val spans = text.getSpans(0, text.length, kind)

			if (spans.isEmpty()) return null

			for (i in spans.size downTo 1) {
				if (text.getSpanFlags(spans[i - 1]) == Spannable.SPAN_MARK_MARK) {
					return spans[i - 1]
				}
			}

			return null
		}
	}

	private class Li
	private class Strike

	override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
		if (opening) {
			if (tag.equals(BULLET_LI, ignoreCase = true)) {
				if (output.isNotEmpty() && output[output.length - 1] != '\n') {
					output.append("\n")
				}
				start(output, Li())
			} else if (tag.equals(STRIKE_THROUGH_S, ignoreCase = true) || tag.equals(STRIKE_THROUGH_STRIKE, ignoreCase = true) || tag.equals(STRIKE_THROUGH_DEL, ignoreCase = true)) {
				start(output, Strike())
			}
		} else {
			if (tag.equals(BULLET_LI, ignoreCase = true)) {
				if (output.isNotEmpty() && output[output.length - 1] != '\n') {
					output.append("\n")
				}
				end(output, Li::class.java, BulletSpan())
			} else if (tag.equals(STRIKE_THROUGH_S, ignoreCase = true) || tag.equals(STRIKE_THROUGH_STRIKE, ignoreCase = true) || tag.equals(STRIKE_THROUGH_DEL, ignoreCase = true)) {
				end(output, Strike::class.java, StrikethroughSpan())
			}
		}
	}

	private fun start(output: Editable, mark: Any) {
		output.setSpan(mark, output.length, output.length, Spanned.SPAN_MARK_MARK)
	}

	private fun end(output: Editable, kind: Class<*>, vararg replaces: Any) {
		val last = getLast(output, kind)
		val start = output.getSpanStart(last)
		val end = output.length
		output.removeSpan(last)

		if (start != end) {
			for (replace in replaces) {
				output.setSpan(replace, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
			}
		}
	}
}
