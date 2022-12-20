package org.tech.town.gripcompany.presentation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.tech.town.gripcompany.databinding.FragmentSearchBinding
import org.tech.town.gripcompany.retrofit.RetrofitManager

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 코드 작성
        binding.button.setOnClickListener {
            Log.d(TAG, "onViewCreated: 검색버튼")
            RetrofitManager.instance.searchMovies("92e32667", "Iron man", completion = {
                responseState, responseBody ->
                when(responseState){
                    RetrofitManager.RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "api호출 성공: $responseBody")
                    }
                    RetrofitManager.RESPONSE_STATE.FAIL -> {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "api호출 실패: $responseBody")
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}