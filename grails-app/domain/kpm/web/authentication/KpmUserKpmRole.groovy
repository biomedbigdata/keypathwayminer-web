package kpm.web.authentication

import org.apache.commons.lang.builder.HashCodeBuilder

class KpmUserKpmRole implements Serializable {

	KpmUser kpmUser
	KpmRole kpmRole

	boolean equals(other) {
		if (!(other instanceof KpmUserKpmRole)) {
			return false
		}

		other.kpmUser?.id == kpmUser?.id &&
			other.kpmRole?.id == kpmRole?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (kpmUser) builder.append(kpmUser.id)
		if (kpmRole) builder.append(kpmRole.id)
		builder.toHashCode()
	}

	static KpmUserKpmRole get(long kpmUserId, long kpmRoleId) {
		find 'from KpmUserKpmRole where kpmUser.id=:kpmUserId and kpmRole.id=:kpmRoleId',
			[kpmUserId: kpmUserId, kpmRoleId: kpmRoleId]
	}

	static KpmUserKpmRole create(KpmUser kpmUser, KpmRole kpmRole, boolean flush = false) {
		new KpmUserKpmRole(kpmUser: kpmUser, kpmRole: kpmRole).save(flush: flush, insert: true)
	}

	static boolean remove(KpmUser kpmUser, KpmRole kpmRole, boolean flush = false) {
		KpmUserKpmRole instance = KpmUserKpmRole.findByKpmUserAndKpmRole(kpmUser, kpmRole)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(KpmUser kpmUser) {
		executeUpdate 'DELETE FROM KpmUserKpmRole WHERE kpmUser=:kpmUser', [kpmUser: kpmUser]
	}

	static void removeAll(KpmRole kpmRole) {
		executeUpdate 'DELETE FROM KpmUserKpmRole WHERE kpmRole=:kpmRole', [kpmRole: kpmRole]
	}

	static mapping = {
		id composite: ['kpmRole', 'kpmUser']
		version false
	}
}
