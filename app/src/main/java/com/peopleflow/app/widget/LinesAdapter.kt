package com.peopleflow.app.widget


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.peopleflow.app.R
import com.peopleflow.app.entities.Line


class LinesAdapter: RecyclerView.Adapter<LinesAdapter.ViewHolder>() {

    var models: List<Line>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var listener: ((Int) -> Unit)? = null

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.id)
        val input: TextView = view.findViewById(R.id.input)
        val ouput: TextView = view.findViewById(R.id.output)
        val btnRemove: ImageView = view.findViewById(R.id.remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): LinesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_line, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.btnRemove.setOnClickListener {
            listener?.invoke(position)
        }
    }



    override fun getItemCount(): Int {
        return models?.size ?: 0
    }

}
