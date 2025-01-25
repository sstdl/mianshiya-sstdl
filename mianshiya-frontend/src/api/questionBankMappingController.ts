// @ts-ignore
/* eslint-disable */
import request from "@/libs/request";

/** addQuestionBankMapping POST /api/questionBankMapping/add */
export async function addQuestionBankMappingUsingPost(
  body: API.QuestionBankMappingAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/api/questionBankMapping/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteQuestionBankMapping POST /api/questionBankMapping/delete */
export async function deleteQuestionBankMappingUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/questionBankMapping/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** getQuestionBankMappingVOById GET /api/questionBankMapping/get/vo */
export async function getQuestionBankMappingVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionBankMappingVOByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseQuestionBankMappingVO_>(
    "/api/questionBankMapping/get/vo",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** listQuestionBankMappingByPage POST /api/questionBankMapping/list/page */
export async function listQuestionBankMappingByPageUsingPost(
  body: API.QuestionBankMappingQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageQuestionBankMapping_>(
    "/api/questionBankMapping/list/page",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** listQuestionBankMappingVOByPage POST /api/questionBankMapping/list/page/vo */
export async function listQuestionBankMappingVoByPageUsingPost(
  body: API.QuestionBankMappingQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageQuestionBankMappingVO_>(
    "/api/questionBankMapping/list/page/vo",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** listMyQuestionBankMappingVOByPage POST /api/questionBankMapping/my/list/page/vo */
export async function listMyQuestionBankMappingVoByPageUsingPost(
  body: API.QuestionBankMappingQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageQuestionBankMappingVO_>(
    "/api/questionBankMapping/my/list/page/vo",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** removeQuestionBankMapping POST /api/questionBankMapping/remove */
export async function removeQuestionBankMappingUsingPost(
  body: API.QuestionBankMappingRemoveRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/questionBankMapping/remove", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** updateQuestionBankMapping POST /api/questionBankMapping/update */
export async function updateQuestionBankMappingUsingPost(
  body: API.QuestionBankMappingUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/questionBankMapping/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
