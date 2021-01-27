import VideoPlayer from "./video-player";

export default class VideoPlayerConsumer {
  constructor() {
    this.videoPlayer = new VideoPlayer();
  }

  playSomethingCool() {
    const coolVideoFileName = "song.mp4";
    this.videoPlayer.playVideoFile(coolVideoFileName);
  }
}
