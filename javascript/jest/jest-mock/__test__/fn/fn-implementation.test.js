import { expect } from "@jest/globals";

describe.skip("mock implementation test", () => {
  test("should create mock", () => {
    const mock = jest
      .fn(() => "third call")
      .mockImplementationOnce(() => "first call")
      .mockImplementationOnce(() => "second call")
      .mockName("mock function");

    expect(mock()).toBe("first call");
    expect(mock()).toBe("second call");
    expect(mock()).toBe("third call");
    expect(mock()).toBe("third call");
    expect(mock()).toBe("third call");
  });
});
