import { expect } from "@jest/globals";

describe.skip("mock return test", () => {
  // mock 함수는 임의의 리턴값을 반환할 수 있다
  test("should return", () => {
    const mock = jest.fn();

    mock
      .mockReturnValueOnce(10)
      .mockReturnValueOnce(20)
      .mockReturnValueOnce(30)
      .mockReturnValue(50);

    expect(mock()).toBe(10);
    expect(mock()).toBe(20);
    expect(mock()).toBe(30);
    expect(mock()).toBe(50);
  });

  test("should filter", () => {
    const mockFilter = jest
      .fn()
      .mockReturnValueOnce(false)
      .mockReturnValueOnce(false)
      .mockReturnValue(true);

    const result = [1, 2, 3].filter((number) => mockFilter(number));
    console.log(mockFilter.mock.calls);

    // 호출횟수 3회
    expect(mockFilter.mock.calls.length).toBe(3);
    // 첫번째 호출의 인자
    expect(mockFilter.mock.calls[0][0]).toBe(1);
    // 두번째 호출의 인자
    expect(mockFilter.mock.calls[1][0]).toBe(2);
    // 세번째 호출의 인자
    expect(mockFilter.mock.calls[2][0]).toBe(3);
    // 결과값
    expect(result).toContain(3);
  });
});
