package com.example.al_pc.recipescreator.Adapters

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import com.example.al_pc.recipescreator.data.Ingredient
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.IngredientName
import kotlinx.android.synthetic.main.ingredient_db_item.view.*

class IngredientsDBAdapter(list:MutableList<IngredientName>): RecyclerView.Adapter<IngredientsDBAdapter.ViewHolder>(){

    lateinit var textName: AppCompatEditText
    lateinit var textCount: AppCompatEditText
    lateinit var textDimension: AppCompatEditText
    lateinit var ingredients: RecyclerView
    lateinit var context: Context
    var isChanged: Boolean = false
    var mItems: MutableList<IngredientName> = list

    var selectedIngredientsList: MutableList<Ingredient> = mutableListOf()

    val selectedIngredientsAdapter: SelectedIngredientsAdapter = SelectedIngredientsAdapter(selectedIngredientsList)

    lateinit var ingredient: Ingredient

    lateinit var llm:LinearLayoutManager

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.ingredient.text = item.ingredient
        if(!textCount.text.isEmpty() && !textDimension.text.isEmpty()) {
            holder.button.setOnClickListener {
                if(textCount.text.toString().trim().isEmpty()){
                    Toast.makeText(context, "Осталось ввести количество", Toast.LENGTH_SHORT).show()
                }else if(textDimension.text.toString().trim().isEmpty()){
                    Toast.makeText(context, "Осталось ввести размерность", Toast.LENGTH_SHORT).show()
                }else if(textCount.text.toString().trim().isEmpty() && textDimension.text.toString().trim().isEmpty()){
                    Toast.makeText(context, "Осталось ввести количество и размерность", Toast.LENGTH_SHORT).show()
                }else {
                    ingredient = Ingredient(item.ingredient, textCount.text.toString(), textDimension.text.toString())

                    selectedIngredientsList.add(ingredient)
                    selectedIngredientsAdapter.notifyData(selectedIngredientsList)

                    textName.text.clear()
                    textCount.text.clear()
                    textDimension.text.clear()
                    isChanged = true
                    mItems.clear()
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.ingredient_db_item, parent, false)
        llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        ingredients.layoutManager = llm
        ingredients.adapter = selectedIngredientsAdapter
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<IngredientName>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ingredient = itemView.ingredient
        val button = itemView.ingredientBTN
    }
}