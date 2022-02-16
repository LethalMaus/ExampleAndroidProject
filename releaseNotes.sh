git fetch origin "+refs/heads/*:refs/remotes/origin/*"
PREVIOUS_APP_VERSION=$(grep PREVIOUS_VERSION build.properties|cut -d'=' -f2)
PREVIOUS_APP_BRANCH=$(git branch -r | grep ${PREVIOUS_APP_VERSION}$)
CURRENT_APP_VERSION=$(grep VERSION_NAME build.properties|cut -d'=' -f2)
CURRENT_APP_BRANCH=$(git branch -r | grep ${CURRENT_APP_VERSION}$)
git log --no-merges --format=%B $PREVIOUS_APP_BRANCH..$CURRENT_APP_BRANCH > release-notes.txt