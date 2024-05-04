package com.example.recreasphere.Model;

public class EventMember {
    String memberName,memberId;

    public EventMember(String memberName, String memberId) {
        this.memberName = memberName;
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberId() {
        return memberId;
    }

}
