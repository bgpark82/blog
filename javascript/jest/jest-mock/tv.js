import Video from "./video";

export default class TV {
    constructor() {
        this.video = new Video();
    }

    play(video) {
        this.video.play(video);
    }
}

