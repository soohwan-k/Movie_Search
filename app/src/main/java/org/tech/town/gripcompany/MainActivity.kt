package org.tech.town.gripcompany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.tech.town.gripcompany.databinding.ActivityMainBinding
import org.tech.town.gripcompany.presentation.favorite.FavoriteFragment
import org.tech.town.gripcompany.presentation.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBottomNavigation()

    }

    // BottomNavigation 초기화 및 선택 버튼 설정 함수
    private fun initBottomNavigation() {
        binding.bottomNavigation.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.searchTab -> changeFragment(SearchFragment())
                    R.id.favoriteTab -> changeFragment(FavoriteFragment())
                }
                true
            }
            selectedItemId = R.id.searchTab // 초기값 세팅
        }
    }

    // fragment 변경 함수
    private fun changeFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            replace(binding.mainFrameLayout.id, fragment)
            commit()
        }
    }
}
