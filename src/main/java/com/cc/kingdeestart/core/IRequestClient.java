package com.cc.kingdeestart.core;

/**
 * @author seven up
 * @date 2023年07月10日 2:30 PM
 */
@KingDeeApiClient(name = "kingDeeIRequest")
public interface IRequestClient {

    String save(String json);

    String saveGroup(String json);

    String deleteGroup(String json);

    String delete(String json);

    String unAudit(String json);

    String submit(String json);

    String audit(String json);

    String queryBill(String json);

    String queryGroupInfo(String json);
}
