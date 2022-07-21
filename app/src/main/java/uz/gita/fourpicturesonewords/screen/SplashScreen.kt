package uz.gita.fourpicturesonewords.screen

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import uz.gita.fourpicturesonewords.R
import uz.gita.fourpicturesonewords.databinding.SplashScreenBinding


class SplashScreen : Fragment() {
    var binding: SplashScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SplashScreenBinding.inflate(inflater, container, false)
        val countDownTimer: CountDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                findNavController(requireView()).navigate(R.id.action_splashScreen_to_mainScreen)
            }
        }
        countDownTimer.start()
        return binding!!.root
    }
}