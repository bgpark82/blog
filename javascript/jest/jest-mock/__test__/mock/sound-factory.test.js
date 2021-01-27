import SoundPlayer from "../../sound-player";
import SoundPlayerConsumer from "../../sound-player-consumer";

const mockPlaySoundFile = jest.fn();
jest.mock("../../sound-player", () => {
  return jest.fn().mockImplementation(() => ({
    playSoundFile: mockPlaySoundFile,
  }));
});

describe.skip("Sound mock factory test", () => {
  test("should have call once", () => {
    const soundPlayerConsumer = new SoundPlayerConsumer();
    soundPlayerConsumer.playSomethingCool();
    expect(mockPlaySoundFile).toHaveBeenCalledTimes(1);
  });
});
