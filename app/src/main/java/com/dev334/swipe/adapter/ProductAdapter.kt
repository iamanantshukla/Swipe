package com.dev334.swipe.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev334.swipe.databinding.CardLayoutProductBinding
import com.dev334.swipe.model.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var productList = ArrayList<Product>()
    fun setProductList(productList : List<Product>){
        this.productList = productList as ArrayList<Product>
        notifyDataSetChanged()
    }
    class ViewHolder(val binding : CardLayoutProductBinding) : RecyclerView.ViewHolder(binding.root) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardLayoutProductBinding.inflate((
                LayoutInflater.from(
                    parent.context
                )
            )
        ))
    }

    fun updateToFilteredList(productList: ArrayList<Product>){
        this.productList = productList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("AdapterDebugging", "onBindViewHolder: "+productList[position].product_name)
        holder.binding.textProductName.text = productList[position].product_name;
        holder.binding.textProductType.text = productList[position].product_type;
        //holder.binding.textTax.text = productList[position].tax.toString();
        holder.binding.textProductPrice.text = "₹ " + productList[position].price.toString();
        val url: String? = productList[position].image
        if(!url.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(url)
                .into(holder.binding.imageProduct)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }
}
