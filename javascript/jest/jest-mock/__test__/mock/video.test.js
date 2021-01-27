import { expect, jest } from "@jest/globals";
import VideoPlayer from "../../video-player";
import VideoPlayerConsumer from "../../video-player-consumer";

jest.mock("../../video-player");

describe.skip("video test", () => {
    test("should play video", () => {
        const videoPlayerConsumer = new VideoPlayerConsumer();
        console.log(VideoPlayer.mock);
        // VideoPlayer 인스턴스 생성횟수
        expect(VideoPlayer.mock.instances.length).toBe(1);
        // VideoPlayer 인스턴스의 playVideoFile 함수는 아직 호출되지 않았다
        expect(
            VideoPlayer.mock.instances[0].playVideoFile
        ).toHaveBeenCalledTimes(0);

        videoPlayerConsumer.playSomethingCool();
        const playVideoFile = VideoPlayer.mock.instances[0].playVideoFile;
        console.log(playVideoFile.mock);
        // VideoPlayer 인스턴스의 playVideoFile 함수는 1회 호출
        expect(playVideoFile.mock.calls.length).toBe(1);
    });
});
