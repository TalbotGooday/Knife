package io.github.mthli.knifedemo

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import io.github.mthli.knifedemo.utils.extensions.toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
	companion object {
		private const val BOLD = "<b>Bold</b><br><br>"
		private const val ITALIC = "<i>Italic</i><br><br>"
		private const val UNDERLINE = "<u>Underline</u><br><br>"
		private const val STRIKE_THROUGH = "<s>Strikethrough</s><br><br>" // <s> or <strike> or <del>
		private const val BULLET = "<ul><li>asdfg</li></ul>"
		private const val QUOTE = "<blockquote>Quote</blockquote>"
		private const val LINK = "<a href=\"https://github.com/mthli/Knife\">Link</a><br><br>"
		private const val EXAMPLE = BOLD + ITALIC + UNDERLINE + STRIKE_THROUGH + BULLET + QUOTE + LINK
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		// ImageGetter coming soon...
		knife.fromHtml(EXAMPLE)
		knife.setSelection(knife.editableText.length)

		setupBold()
		setupItalic()
		setupUnderline()
		setupStrikeThrough()
		setupBullet()
		setupQuote()
		setupLink()
		setupClear()
	}

	private fun setupBold() {
		bold.setOnClickListener { knife.bold() }

		bold.setOnLongClickListener {
			toast(R.string.toast_bold)
			true
		}
	}

	private fun setupItalic() {
		italic.setOnClickListener { knife.italic() }

		italic.setOnLongClickListener {
			toast(R.string.toast_italic)
			true
		}
	}

	private fun setupUnderline() {
		underline.setOnClickListener { knife.underline() }

		underline.setOnLongClickListener {
			toast(R.string.toast_underline)
			true
		}
	}

	private fun setupStrikeThrough() {
		strikethrough.setOnClickListener { knife.strikeThrough() }

		strikethrough.setOnLongClickListener {
			toast(R.string.toast_strikethrough)
			true
		}
	}

	private fun setupBullet() {
		bullet.setOnClickListener { knife.bullet() }

		bullet.setOnLongClickListener {
			toast(R.string.toast_bullet)
			true
		}
	}

	private fun setupQuote() {
		quote.setOnClickListener { knife.quote() }

		quote.setOnLongClickListener {
			toast(R.string.toast_quote)
			true
		}
	}

	private fun setupLink() {
		link.setOnClickListener { showLinkDialog() }

		link.setOnLongClickListener {
			toast(R.string.toast_insert_link)
			true
		}
	}

	private fun setupClear() {
		clear.setOnClickListener { knife.clearFormats() }

		clear.setOnLongClickListener {
			toast(R.string.toast_format_clear)
			true
		}
	}

	private fun showLinkDialog() {
		val start = knife.selectionStart
		val end = knife.selectionEnd

		val builder = AlertDialog.Builder(this)
		builder.setCancelable(false)

		val view = layoutInflater.inflate(R.layout.dialog_link, null, false)

		val editText = view.findViewById<EditText>(R.id.edit)

		builder.setView(view)
		builder.setTitle(R.string.dialog_title)

		builder.setPositiveButton(R.string.dialog_button_ok, DialogInterface.OnClickListener { _, _ ->
			val link = editText.text.toString().trim { it <= ' ' }
			if (TextUtils.isEmpty(link)) {
				return@OnClickListener
			}

			// When KnifeText lose focus, use this method
			knife.link(link, start, end)
		})

		builder.setNegativeButton(R.string.dialog_button_cancel) { _, _ ->
			// DO NOTHING HERE
		}

		builder.create().show()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.undo -> knife.undo()
			R.id.redo -> knife.redo()
			R.id.github -> {
				val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.app_repo)))
				startActivity(intent)
			}
			else -> {
			}
		}

		return true
	}
}
