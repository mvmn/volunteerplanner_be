package com.volunteer.api.data.model;

public enum UserAuthority {

  USERS_VIEW,
  USERS_VERIFY,
  USERS_LOCK,

  CATEGORIES_VIEW,
  CATEGORIES_MODIFY,

  STORES_VIEW_PUBLIC,
  STORES_VIEW_CONFIDENTIAL,
  STORES_MODIFY,

  PRODUCTS_VIEW,
  PRODUCTS_MODIFY,

  TASKS_VIEW,
  TASKS_MODIFY,
  TASKS_VERIFY,
  TASKS_COMPLETE,
  TASKS_REJECT,

  SUBTASKS_VIEW,
  SUBTASKS_VIEW_MINE,
  SUBTASKS_MODIFY_MINE,
  SUBTASKS_COMPLETE,
  SUBTASKS_REJECT,
  SUBTASKS_REJECT_MINE

}
