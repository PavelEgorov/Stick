package com.egorovsoft.stick.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.egorovsoft.stick.R
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Note.Color

class MainAdapter(val onItemViewClick : ((note: Note) -> Unit)? = null) : RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )


    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(notes[position])

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.title)
        private val body = itemView.findViewById<TextView>(R.id.body)

        fun bind(note: Note) {
            title.text = note.title
            body.text = note.note
            val color = when (note.color) {
                Color.WHITE -> R.color.color_white
                Color.YELLOW -> R.color.color_yello
                Color.RED -> R.color.color_red
                Color.GREEN -> R.color.color_green
                Color.BLUE -> R.color.color_blue
                else -> R.color.color_green
            }

            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, color))

            itemView.setOnClickListener { onItemViewClick?.invoke(note) }
        }
    }
}

