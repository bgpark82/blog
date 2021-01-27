import { beforeEach, expect } from "@jest/globals";
import SoundPlayer from "../../sound-player";
import SoundPlayerConsumer from "../../sound-player-consumer";

jest.mock("../../sound-player");

// 기존의 SoundPlayer mock 객체에 원하는 mock 함수 넣기
describe.skip("Sound mock factory test", () => {
  beforeEach(() => {
    SoundPlayer.mockImplementation(() => ({
      playSoundFile: () => {
        throw new Error("error");
      },
    }));
  });

  test("should have call once", () => {
    const soundPlayerConsumer = new SoundPlayerConsumer();
    expect(() => soundPlayerConsumer.playSomethingCool()).toThrow();
  });
});
