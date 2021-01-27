export default class VideoPlayer {
  constructor() {
    this.foo = "bar";
  }

  playVideoFile(fileName) {
    console.log("Playing sound file " + fileName);
    return fileName;
  }
}
