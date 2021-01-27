import { expect, jest } from "@jest/globals";

import SoundPlayer, { mockPlaySoundFile } from "../../sound-player";
import SoundPlayerConsumer from "../../sound-player-consumer";

jest.mock("../../sound-player");

describe.skip("sound __mock__ test", () => {
  test("should call mock", () => {
    console.log(mockPlaySoundFile);
    console.log(SoundPlayer);
    new SoundPlayerConsumer();
  });
});
