package aslan.aslanov.prayerapp.ui.fragment.city.adapterCity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.prayerapp.databinding.LayoutItemCityBinding
import aslan.aslanov.prayerapp.model.countryModel.City

class ViewHolderCity private constructor(private val binding: LayoutItemCityBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        city: City,
        onClickListener: (City) -> Unit
    ): Unit = with(binding) {
        cityItem = city
        root.setOnClickListener {
            onClickListener(city)
        }
        executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderCity {
            val inflater = LayoutInflater.from(parent.context)
            val root = LayoutItemCityBinding.inflate(inflater, parent, false)
            return ViewHolderCity(root)
        }
    }
}
