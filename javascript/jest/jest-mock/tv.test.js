import { expect, jest } from "@jest/globals";
import Video from "./video";
import TV from "./tv";

jest.mock("./video");

describe("TV Test", () => {
    test("should play video", () => {
        const tv = new TV();
        console.log(Video);
    });
});
