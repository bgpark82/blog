import SoundPlayer from "../../sound-player";
import SoundPlayerConsumer from "../../sound-player-consumer";

jest.mock("../../sound-player");

describe.skip("Sound test", () => {
  beforeEach(() => {
    SoundPlayer.mockClear();
  });

  // mockClear 되었으므로 SoundPlayer는 호출되면 안된다
  test("should not call SoundPlayer", () => {
    console.log(SoundPlayer);
    expect(SoundPlayer).not.toHaveBeenCalled();
  });

  // SoundPlayerConsumer 생성자 생성 시점에 SoundPlayer 또한 생성되므로 한번 호출된다
  test("should call SoundPlayer Once", () => {
    new SoundPlayerConsumer();
    expect(SoundPlayer).toHaveBeenCalledTimes(1);
  });

  test("should check mock", () => {
    const soundPlayerConsumer = new SoundPlayerConsumer();
    /**
     *  _isMockFunction: true,
     *  getMockImplementation: [Function],
     *  mock: [Getter/Setter],
     *  mockClear: [Function],
     *  mockReset: [Function],
     *  mockRestore: [Function],
     *  mockReturnValueOnce: [Function],
     *  mockResolvedValueOnce: [Function],
     *  mockRejectedValueOnce: [Function],
     *  mockReturnValue: [Function],
     *  mockResolvedValue: [Function],
     *  mockRejectedValue: [Function],
     *  mockImplementationOnce: [Function],
     *  mockImplementation: [Function],
     *  mockReturnThis: [Function],
     *  mockName: [Function],
     *  getMockName: [Function]
     */
    console.log(SoundPlayer.mock);

    soundPlayerConsumer.playSomethingCool();
    /**
     *  calls: [ [] ],
     *  instances: [ SoundPlayer { playSoundFile: [Function] } ],
     *  invocationCallOrder: [ 2 ],
     *  results: [ { type: 'return', value: undefined } ]
     */
    console.log(SoundPlayer.mock.instances[0]);

    const soundPlayerInstance = SoundPlayer.mock.instances[0];
    console.log(soundPlayerInstance);
    const playSoundFile = soundPlayerInstance.playSoundFile;
    console.log(playSoundFile);

    expect(playSoundFile.mock.calls[0][0]).toBe("song.mp3");

    expect(playSoundFile).toHaveBeenCalledTimes(1);
    expect(playSoundFile).toHaveBeenCalledWith("song.mp3");
  });
});
