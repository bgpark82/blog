import { expect, jest } from "@jest/globals";

describe.skip("fn mock test", () => {
  // mock 인스턴스의 this도 추적 가능
  test("should be instantiate", () => {
    const mockClass = jest.fn();

    // mock 클래스의 첫번째 인스턴스화
    const a = new mockClass();

    const b = { a: "a" };
    const bound = mockClass.bind(b);
    // mock 클래스의 bind 이후 두번째 인스턴스화
    bound();
    console.log(mockClass.mock.instances);

    // mock 함수는 2번 인스턴스화 되었다 -> this 추적 가능
    expect(mockClass.mock.instances.length).toBe(2);

    // mock 함수가 2번째 인스턴스화 되었을 때, this의 프로퍼티 중 a의 값은 a이다
    expect(mockClass.mock.instances[1].a).toBe("a");
  });
});
