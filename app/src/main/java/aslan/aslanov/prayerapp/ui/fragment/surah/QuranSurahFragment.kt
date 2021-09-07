package aslan.aslanov.prayerapp.ui.fragment.surah

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentQuranSurahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranBinding
import aslan.aslanov.prayerapp.ui.fragment.surah.adapter.QuranAdapter

class QuranSurahFragment : Fragment() {

    private val binding by lazy { FragmentQuranSurahsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<QuranSurahsViewModel>()
    private lateinit var quranAdapter: QuranAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeQuranData()
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@QuranSurahFragment
        setHasOptionsMenu(true)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeQuranData(): Unit = with(viewModel) {
        fetchQuran("az.mammadaliyev")
        quranResponse.observe(viewLifecycleOwner, {
            it?.let {
                quranAdapter = QuranAdapter(it) { viewDataBinding, list, i, surah ->
                    if (viewDataBinding is LayoutItemQuranBinding) {
                        viewDataBinding.quranItem = surah
                        viewDataBinding.root.setOnClickListener { v->
                            val action=QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranAyahs().setSurahNum(surah.number!!)
                            v.findNavController().navigate(action)
                        }
                    }

                }.apply { notifyDataSetChanged() }
                binding.recyclerViewQuran.adapter = quranAdapter
            }
        })

        quranUiState.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.progressBarQuran.visibility = View.VISIBLE
                } else {
                    binding.progressBarQuran.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quran_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lang_arb_menu -> {
                viewModel.fetchQuran("ar.alafasy")
            }
            R.id.lang_aze_menu -> {
                viewModel.fetchQuran("az.mammadaliyev")
            }
            R.id.lang_tr_menu -> {
                viewModel.fetchQuran("tr.diyanet")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

    }
}