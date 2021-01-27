import { expect, jest } from "@jest/globals";
import axios from "axios";
import Users from "../../user";

jest.mock("axios");

describe.skip("user test", () => {
  test("should call axios", () => {
    const users = [{ name: "bob" }];
    const res = { data: users };

    axios.get.mockImplementation(() => Promise.resolve(res));

    return Users.all().then((data) => expect(data).toBe(users));
  });
});
