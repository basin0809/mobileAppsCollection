package Assignment4;

import CompositePrimaryKey.Locater;

/**
 * A MembershipEmbeddedId specifies a person and team.
 * @author Xipeng Wang
 */
public class MembershipEmbeddedId {
    /** The person. */
    Integer hasMember;
    /** The team. */
    Integer isMemberOf;
    
    /**
     * Test for equality of two membershipEmbeddedIds.
     * @param object The object with which to compare.
     * @return Whether the object is the same membershipEmbeddedId as this one.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MembershipEmbeddedId)) {
            return false;
        }
       
        MembershipEmbeddedId membershipEmbeddedId = (MembershipEmbeddedId)object;
        return hasMember.equals(membershipEmbeddedId.hasMember) 
        		&& isMemberOf.equals(membershipEmbeddedId.isMemberOf);
    }

    /**
     * Compute a hash value compatible with equality.
     * @return The hash value of the membershipEmbeddedId.
     */
    @Override
    public int hashCode() {
        return hasMember.hashCode() + isMemberOf.hashCode();
    }
}
