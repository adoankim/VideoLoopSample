package kim.adoan.videoloopsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exoPlayer?.let {
            it.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
            it.player = buildPlayerForRawVideo(R.raw.video)
        }
    }

    private fun buildPlayerForRawVideo(rawVideoId : Int, loop: Boolean = true, resizeCropping: Boolean = true): SimpleExoPlayer =
            ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector()).apply {
                val mediaSource = getMediaSource(rawVideoId)
                playWhenReady = true

                videoScalingMode = if (resizeCropping) {
                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                } else {
                    C.VIDEO_SCALING_MODE_DEFAULT
                }

                prepare(if (loop) {
                    LoopingMediaSource(mediaSource)
                } else {
                    mediaSource
                })
            }


    private fun getMediaSource(rawVideoId: Int): MediaSource? {
        val uri = RawResourceDataSource.buildRawResourceUri(rawVideoId)

        val dataSource = RawResourceDataSource(this)
        dataSource.open(DataSpec(uri))

        return ExtractorMediaSource(uri,
                DataSource.Factory { dataSource },
                Mp4Extractor.FACTORY,
                null,
                null)
    }

}
